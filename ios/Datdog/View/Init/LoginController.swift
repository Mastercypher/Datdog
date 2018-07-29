//
//  RegistrationController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 24/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit

class LoginController : UIViewController, UITextFieldDelegate {
    
    @IBOutlet weak var txfLogin: UITextField!
    @IBOutlet weak var txfPassword: UITextField!
    var mEmail = String("")
    var mPassword = String("")
    
    let db = UserDbManager()
    var mUser: User? = nil
    
    override func loadView() {
        mUser = db.getCurrent(view: self)
        if (mUser != nil) {
            self.goToMainNavigation()
        } else {
            super.loadView()
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        mUser = db.getCurrent(view: self)
        if (mUser != nil) {
            self.goToMainNavigation()
        }
    }
    
    @IBAction func eventLogin(_ sender: Any) {
        var errorMessage = String("")
        mEmail = txfLogin.text!
        mPassword = txfPassword.text!
        if(mEmail.count > 0 && mPassword.count > 0){
            self.verifyLogin()
        } else {
            errorMessage = "Insert email and password"
        }
        if(!errorMessage.isEmpty){
            let alert = UIAlertController(title: "Attention", message: errorMessage, preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: NSLocalizedString("Ok", comment: "Default action"), style: .default, handler: nil))
            self.present(alert, animated: true, completion: nil)
        }
        
    }
    
    // Permit to oush the registration data online.
    func verifyLogin(){
        let urlString = "http://datdog.altervista.org/user.php?action=select-login&email_u=\(mEmail)&password_u=\(mPassword)"
        let url = URL(string: urlString)!
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let task = URLSession.shared.dataTask(with: request) { [unowned self](data, response, error) in
            
             if (data != nil) {
             let string = String(data: data!, encoding: .utf8)
             debugPrint(string!)
             }
            
            
            do {
                let respond = try JSONSerialization.jsonObject(with: data!, options: JSONSerialization.ReadingOptions.allowFragments)
                let dict = respond as! [String : Any]
                DispatchQueue.main.async {
                    self.loginUser(dictionary: dict)
                }
            } catch {
                debugPrint()
            }
        }
        
        task.resume()
    }
    
    func loginUser(dictionary: [String: Any]) {
        var loginSuccess = false
        let success = dictionary["success"] as? Bool
        let usersDic = dictionary["additional"] as? [[String: String]]
        let countUsersDic = usersDic?.count
        
        if (success! && countUsersDic != nil && countUsersDic! > 0) {
            for user in usersDic!{
                let userDb = UserDbManager()
                let user = User(id: Int(user["id"]!)!, name: user["name_u"]!, surname: user["surname_u"]!, phone: user["phone_u"]!, birth: user["birth_u"]!,
                                email: user["email_u"]!, password: user["password_u"]!, dateCreate: user["date_create_u"]!, dateUpdate: user["date_update_u"]!,
                                delete: Int(user["delete_u"]!)!)
                
                if(user.mDelete == UserDbManager.STATUS.ACTIVE && userDb.login(user: user)){
                    loginSuccess = true
                }
                
                break
            }
        }
        
        if(loginSuccess){
            self.goToMainNavigation()
        } else {
            let alert = UIAlertController(title: "Attention", message: "Email or password wrong", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: NSLocalizedString("Ok", comment: "Default action"), style: .default, handler: nil))
            self.present(alert, animated: true, completion: nil)
        }
    }
    
    func goToMainNavigation(){
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "mainNav") as! UITabBarController
        self.present(vc, animated: false, completion:nil )
    }
}
