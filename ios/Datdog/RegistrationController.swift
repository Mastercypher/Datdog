//
//  RegistrationController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 24/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit

class RegistrationController : UIViewController, UITextFieldDelegate {
    
    @IBOutlet weak var txfName: UITextField!
    @IBOutlet weak var txfSurname: UITextField!
    @IBOutlet weak var txfPhone: UITextField!
    @IBOutlet weak var txfBirth: UITextField!
    @IBOutlet weak var txfEmail: UITextField!
    @IBOutlet weak var txfPassword: UITextField!
    @IBOutlet weak var txfRepeatPassword: UITextField!
    @IBOutlet weak var btnRegistre: UIButton!
    
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var contentView: UIView!
    @IBOutlet weak var constraintContentHeight: NSLayoutConstraint!
    
    var activeField: UITextField?
    var lastOffset: CGPoint!
    var keyboardHeight: CGFloat!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        // Set textfield delegate
        txfName.delegate = self
        txfSurname.delegate = self
        txfPhone.delegate = self
        txfBirth.delegate = self
        txfEmail.delegate = self
        txfPassword.delegate = self
        txfRepeatPassword.delegate = self
        
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillChange(notification:)), name: NSNotification.Name.UIKeyboardDidShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillChange(notification:)), name: NSNotification.Name.UIKeyboardDidHide, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillChange(notification:)), name: NSNotification.Name.UIKeyboardWillChangeFrame, object: nil)
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name.UIKeyboardDidShow, object: nil)
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name.UIKeyboardDidHide, object: nil)
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name.UIKeyboardWillChangeFrame, object: nil)
    }
    
    func hideKeyboard(){
        txfName.resignFirstResponder()
        txfSurname.resignFirstResponder()
        txfPhone.resignFirstResponder()
        txfBirth.resignFirstResponder()
        txfEmail.resignFirstResponder()
        txfPassword.resignFirstResponder()
        txfRepeatPassword.resignFirstResponder()
        txfRepeatPassword.resignFirstResponder()
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        hideKeyboard()
        return true
    }
    
    @objc func keyboardWillChange(notification: Notification){
        guard let keyboardReact = (notification.userInfo?[UIKeyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue else {
            return
        }
        print(keyboardReact.height)
        if (notification.name == Notification.Name.UIKeyboardWillShow || notification.name == Notification.Name.UIKeyboardWillChangeFrame) {
            view.frame.origin.y = -keyboardReact.height
            print(keyboardReact.height)
        } else {
            view.frame.origin.y = 0
            print(0)

        }
    }
    
    
}
