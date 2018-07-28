//
//  Date.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 26/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import Foundation
import UIKit

class UtilProj {
    
    struct DBSTATUS {
        static let AVAILABLE = 0
        static let DELETE = 1
    }
    
    struct ERR {
        static let SAVING = "Problem encountered while saving data"
        static let LOGOUT = "Problem encountered, you will return to the login screen to try again."
        static let GENERAL_LOGOUT = "Problem encountered, restart the application and try again."
        
        static let CHAR_MIN = "You have to fill all the field"
        static let CHAR_MAX = "Max characters for any field is 50"
    }
    
    static func getDateNow() -> String{
        let date = Date()
        let formatter = DateFormatter()
        formatter.dateFormat = "dd/MM/yyyy-HH:mm:ss"
        return formatter.string(from: date)
    }
    
    static func showAlertOk(view: UIViewController, title: String, message: String, handler: ((UIAlertAction) -> Swift.Void)? = nil) {
        let alert = UIAlertController(title: title, message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("Ok", comment: "Logout action"), style: .default, handler: handler))
        view.present(alert, animated: true, completion: nil)
    }
    
    static func alertLogout(view: UIViewController){
        self.showAlertOk(view: view, title: "Attention", message: self.ERR.LOGOUT, handler:{ _ in
            self.goToLogin(view: view)
        })
    }
    
    static func alertError(view: UIViewController){
        self.showAlertOk(view: view, title: "Attention", message: self.ERR.GENERAL_LOGOUT, handler: nil)
    }
    
    static func goToLogin(view: UIViewController) {
        self.showAlertOk(view: view, title: "Attention", message: self.ERR.LOGOUT, handler:{ _ in
            self.goToLogin(view: view)
        })
        let vc = view.storyboard?.instantiateViewController(withIdentifier: "loginView") as! LoginController
        view.present(vc, animated: true, completion:nil )
    }
    
    static func backNavigation(view: UIViewController) {
        view.navigationController?.popViewController(animated: true)
    }
}
