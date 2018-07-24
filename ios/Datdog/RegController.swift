//
//  RegController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 24/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit

class RegController : UIViewController, UITextFieldDelegate {

    
    @IBOutlet weak var txfFirst: UITextField!
    @IBOutlet weak var txfSecond: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        // Set textfield delegate
        txfFirst.delegate = self
        txfSecond.delegate = self
        
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillChange(notification:)), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillChange(notification:)), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillChange(notification:)), name: NSNotification.Name.UIKeyboardWillChangeFrame, object: nil)
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name.UIKeyboardWillHide, object: nil)
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name.UIKeyboardWillChangeFrame, object: nil)
    }
    
    func hideKeyboard(){
        txfFirst.resignFirstResponder()
        txfSecond.resignFirstResponder()
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        hideKeyboard()
        return true
    }
    
    @objc func keyboardWillChange(notification: Notification){
        print(notification.name.rawValue)
        
        guard let keyboardReact = (notification.userInfo?[UIKeyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue else {
            return
        }
        if (notification.name == Notification.Name.UIKeyboardWillShow || notification.name == Notification.Name.UIKeyboardWillChangeFrame) {
            view.frame.origin.y = -keyboardReact.height
            print(keyboardReact.height)
        } else {
            view.frame.origin.y = 0
            print(0)
            
        }
    }
    
    
}
