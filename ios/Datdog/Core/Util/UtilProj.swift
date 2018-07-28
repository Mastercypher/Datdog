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
    
    struct COLORS {
        static let PRIMARY = "#157EFB"
        static let WARNING = "#FDBF07"
        static let SUCCESS = "#28A745"
        static let DANGER = "#FF3B30"
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
    
    static func transformToDate(dateString: String) -> Date{
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "dd/MM/yyyy-HH:mm:ss"
        dateFormatter.locale = Locale(identifier: "en_US_POSIX") // set locale to reliable US_POSIX
        let date = dateFormatter.date(from:dateString)!
        return date
    }
    
    static func getUIColor(hex: String, alpha: CGFloat = 1) -> UIColor?{
        assert(hex[hex.startIndex] == "#", "Expected hex string of format #RRGGBB")
        
        let scanner = Scanner(string: hex)
        scanner.scanLocation = 1  // skip #
        
        var rgb: UInt32 = 0
        scanner.scanHexInt32(&rgb)
        
        return UIColor(
            red:   CGFloat((rgb & 0xFF0000) >> 16)/255.0,
            green: CGFloat((rgb &   0xFF00) >>  8)/255.0,
            blue:  CGFloat((rgb &     0xFF)      )/255.0,
            alpha: alpha)
    
    }
    
    static func showToast(view: UIViewController, message : String) {
        let toastLabel = UILabel(frame: CGRect(x: view.view.frame.size.width/2 - 75, y: view.view.frame.size.height-100, width: 150, height: 35))
        toastLabel.backgroundColor = UIColor.black.withAlphaComponent(0.6)
        toastLabel.textColor = UIColor.white
        toastLabel.textAlignment = .center;
        toastLabel.font = UIFont(name: "Montserrat-Light", size: 12.0)
        toastLabel.text = message
        toastLabel.alpha = 1.0
        toastLabel.layer.cornerRadius = 10;
        toastLabel.clipsToBounds  =  true
        view.view.addSubview(toastLabel)
        UIView.animate(withDuration: 4.0, delay: 0.1, options: .curveEaseOut, animations: {
            toastLabel.alpha = 0.0
        }, completion: {(isCompleted) in
            toastLabel.removeFromSuperview()
        })
    }
    
    static func showAlertOk(view: UIViewController, title: String, message: String, handler: ((UIAlertAction) -> Swift.Void)? = nil) {
        let alert = UIAlertController(title: title, message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("Ok", comment: "Logout action"), style: .default, handler: handler))
        if handler != nil {
            alert.addAction(UIAlertAction(title: NSLocalizedString("Cancel", comment: "Default action"), style: .default, handler: nil))
        }
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
