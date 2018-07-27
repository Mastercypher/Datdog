//
//  UserInfoController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 27/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit

class ViewUserController: UIViewController {
    
    @IBOutlet weak var lblTitleName: UILabel!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var lblSurname: UILabel!
    @IBOutlet weak var lblPhone: UILabel!
    @IBOutlet weak var lblBirth: UILabel!
    @IBOutlet weak var lblEmail: UILabel!
    
    let db = UserDbManager()
    var mUser: User? = nil
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.prefersLargeTitles = true
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)
        mUser = db.getCurrent(view: self)
        lblTitleName.text = mUser?.mName
        lblName.text = mUser?.mName
        lblSurname.text = mUser?.mSurname
        lblPhone.text = mUser?.mPhone
        lblBirth.text = mUser?.mBirth
        lblEmail.text = mUser?.mEmail
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    @IBAction func touchLogout(_ sender: Any) {
        let view = self
        
        let alert = UIAlertController(title: "Logout", message: "You will be returned to the login screen", preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("Ok", comment: "Logout action"), style: .default, handler: { _ in
            view.db.logut(user: view.mUser!)
            UtilProj.goToLogin(view: view)
        }))
        alert.addAction(UIAlertAction(title: NSLocalizedString("Cancel", comment: "Default action"), style: .default, handler: nil))
        self.present(alert, animated: true, completion: nil)
        
        
    }
}
