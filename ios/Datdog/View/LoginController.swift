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
    
    override func viewDidLoad() {
        super.viewDidLoad()
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
        let urlString = "http://datdog.altervista.org/user.php?action=select&email_u=\(mEmail)&password_u=\(mPassword)"
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
                    self.logedIn(dictionary: dict)
                }
            } catch {
                debugPrint()
            }
        }
        
        task.resume()
    }
    
    func logedIn(dictionary: [String: Any]) {
        let success = dictionary["success"] as? Bool
        
        if (success!) {
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "main_navigation") as! UITabBarController
            self.present(vc, animated: true, completion:nil )
        } else {
            let alert = UIAlertController(title: "Attention", message: "Email or password wrong", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: NSLocalizedString("Ok", comment: "Default action"), style: .default, handler: nil))
            self.present(alert, animated: true, completion: nil)
        }
    }
}
