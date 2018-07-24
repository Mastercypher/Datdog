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
    //This method will adjust your scrollView and will show textFields above the keyboard.
    @objc func keyboardWillShow(aNotification: NSNotification) {
        var info = aNotification.userInfo!
        let kbSize: CGSize = ((info["UIKeyboardFrameEndUserInfoKey"] as? CGRect)?.size)!
        let contentInsets: UIEdgeInsets = UIEdgeInsetsMake(0.0, 0.0, kbSize.height, 0.0)
        var aRect: CGRect = self.view.frame
        
        scrollView.contentInset = contentInsets
        scrollView.scrollIndicatorInsets = contentInsets
        aRect.size.height -= kbSize.height
        print(kbSize.height)
        if (!aRect.contains(activeField!.frame.origin)) {
            scrollView.scrollRectToVisible(activeField!.frame, animated: true)
        }
    }
}
