//
//  RegistrationController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 24/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit
import Foundation


class RegistrationController : UIViewController, UITextFieldDelegate {
    
    @IBOutlet weak var txfName: UITextField!
    @IBOutlet weak var txfSurname: UITextField!
    @IBOutlet weak var txfPhone: UITextField!
    @IBOutlet weak var txfBirth: UITextField!
    @IBOutlet weak var txfEmail: UITextField!
    @IBOutlet weak var txfPassword: UITextField!
    @IBOutlet weak var txfRepeatPassword: UITextField!
    
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var contentView: UIView!
    
    var activeField: UITextField?
    
    var name = String("")
    var surname = String("")
    var phone = String("")
    var birth = String("")
    var email = String("")
    var password = String("")
    var repPassword = String("")
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.prefersLargeTitles = true
        // Set textfield delegate
        txfName.delegate = self
        txfSurname.delegate = self
        txfPhone.delegate = self
        txfBirth.delegate = self
        txfEmail.delegate = self
        txfPassword.delegate = self
        txfRepeatPassword.delegate = self
        
    }
    
    override func viewWillAppear(_ animated:Bool) {
        super.viewWillAppear(animated)
        
        // Add this observers to observe keyboard shown and hidden events
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillBeHidden(aNotification:)), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow(aNotification:)), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        //Remove the observers added for keyboard from your ViewController
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name.UIKeyboardWillHide, object: nil)
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name.UIKeyboardWillShow, object: nil)
    }
    
    // – UITextField Delegates
    // ViewController activeField is an object of UITextField which will be used to manage and resign current active textField
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        activeField = textField
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        activeField = nil
    }
    
    // Called when the UIKeyboardWillHide is sent
    // This method is called from selector. So it requires @objc keyword and this method will adjust your scrollView (here myScrollView)  and textFields to show as original.
    
    @objc func keyboardWillBeHidden(aNotification: NSNotification) {
        let contentInsets: UIEdgeInsets = .zero
        self.scrollView.contentInset = contentInsets
        self.scrollView.scrollIndicatorInsets = contentInsets
        
    }
    
    // Called when the UIKeyboardWillShow is sent
    // This method will adjust your scrollView and will show textFields above the keyboard.
    @objc func keyboardWillShow(aNotification: NSNotification) {
        var info = aNotification.userInfo!
        let kbSize: CGSize = ((info["UIKeyboardFrameEndUserInfoKey"] as? CGRect)?.size)!
        let contentInsets: UIEdgeInsets = UIEdgeInsetsMake(0.0, 0.0, kbSize.height + 30, 0.0)
        var aRect: CGRect = self.view.frame
        
        scrollView.contentInset = contentInsets
        scrollView.scrollIndicatorInsets = contentInsets
        aRect.size.height -= kbSize.height
        
        if (activeField != nil && !aRect.contains(activeField!.frame.origin)) {
            scrollView.scrollRectToVisible(activeField!.frame, animated: true)
        }
    }
    
    /////////////////////
    // END UI Funcions //
    /////////////////////
    
    // Action listener for the registration button.
    @IBAction func touchRegister(_ sender: Any) {
        var errorMessage = ""
        self.name = txfName.text!
        self.surname = txfSurname.text!
        self.phone = txfPhone.text!
        self.birth = txfBirth.text!
        self.email = txfEmail.text!
        self.password = txfPassword.text!
        self.repPassword = txfRepeatPassword.text!
        
        // Check if all the field are more of one character
        if (self.name.count > 0 && self.surname.count > 0 && self.phone.count > 0 && self.birth.count > 0
            && self.email.count > 0 && self.password.count > 0 && self.repPassword.count > 0)  {
            // Check if all the field are less of 50 characters
            if (self.name.count < 50 && self.surname.count < 50 && self.phone.count < 50 && self.birth.count < 50
                && self.email.count < 50 && self.password.count < 50 && self.repPassword.count < 50)  {
                // Check if the repeat password is equal to the password
                if (self.password.count > 3){
                    if (self.password.elementsEqual(self.repPassword)){
                        self.pushRegistration()
                    } else {
                        errorMessage = "Password not matching"
                    }
                } else {
                    errorMessage = UtilProj.ERR.PASW_TO_SHORT
                }
            }else {
                errorMessage = UtilProj.ERR.CHAR_MAX
            }
        } else {
            errorMessage = UtilProj.ERR.CHAR_MIN
        }
        
        if(!errorMessage.isEmpty){
            UtilProj.showAlertOk(view: self, title: "Attention", message: errorMessage, handler: nil)
        }
    }
    
    // Permit to oush the registration data online.
    func pushRegistration(){
        let dateNow = UtilProj.getDateNow()
        let urlString = "http://datdog.altervista.org/user.php?action=insert-login&" +
            "name_u=\(name)&surname_u=\(surname)&phone_u=\(phone)&birth_u=\(birth)&" +
        "email_u=\(email)&password_u=\(password)&date_create_u=\(dateNow)&date_update_u=\(dateNow)&delete_u=0"
        let url = URL(string: urlString)!
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let task = URLSession.shared.dataTask(with: request) { [unowned self](data, response, error) in
            /*
            if (data != nil) {
                let string = String(data: data!, encoding: .utf8)
                debugPrint(string!)
            }*/
            
            do {
                let respond = try JSONSerialization.jsonObject(with: data!, options: JSONSerialization.ReadingOptions.allowFragments)
                let dict = respond as! [String : Any]
                DispatchQueue.main.async {
                    self.endRegistration(dictionary: dict)
                }
            } catch {
                debugPrint(error)
            }
        }
        
        task.resume()
    }
    
    // Called when the task end the execution.
    func endRegistration(dictionary: [String: Any]){
        let success = dictionary["success"] as? Bool
        
        if (success!) {
            let alert = UIAlertController(title: "Success", message: "Compliment, you are in", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: NSLocalizedString("Login", comment: "Default action"), style: .default, handler: { _ in
                let _ = self.navigationController?.popViewController(animated: true)
            }))
            self.present(alert, animated: true, completion: nil)
        } else {
            let alert = UIAlertController(title: "Attention", message: "Email address already in use", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: NSLocalizedString("Ok", comment: "Default action"), style: .default, handler: nil))
            self.present(alert, animated: true, completion: nil)
        }
    }
}
