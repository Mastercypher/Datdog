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
    static let ERR_LOGOUT = "We have encountered a problem, you will return to the login screen to try again."
    static let ERR_GENERAL = "We have encountered a problem, restart the application and try again."

    static func getDateNow() -> String{
        let date = Date()
        let formatter = DateFormatter()
        formatter.dateFormat = "dd/MM/yyyy-HH:mm:ss"
        return formatter.string(from: date)
    }
    
    static func alertLogout(view: UIViewController){
        let alert = UIAlertController(title: "Attention", message: self.ERR_LOGOUT, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("Ok", comment: "Logout action"), style: .default, handler: { _ in
            self.goToLogin(view: view)
        }))
        view.present(alert, animated: true, completion: nil)
    }

    static func alertError(view: UIViewController){
        let alert = UIAlertController(title: "Attention", message: self.ERR_GENERAL, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("Ok", comment: "Default action"), style: .default, handler: nil ))
        view.present(alert, animated: true, completion: nil)
    }
    
    static func goToLogin(view: UIViewController) {
        let vc = view.storyboard?.instantiateViewController(withIdentifier: "loginView") as! LoginController
        view.present(vc, animated: true, completion:nil )
    }

}
