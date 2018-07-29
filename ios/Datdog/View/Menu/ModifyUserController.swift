//
//  UserModifyController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 27/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit

class ModifyUserController: UIViewController {
    
    @IBOutlet weak var txfName: UITextField!
    @IBOutlet weak var txfSurname: UITextField!
    @IBOutlet weak var txfPhone: UITextField!
    @IBOutlet weak var txfBirth: UITextField!
    
    let db = UserDbManager()
    var mUser: User? = nil
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.prefersLargeTitles = true
        mUser = db.getCurrent(view: self)
        txfName.text = mUser?.mName
        txfSurname.text = mUser?.mSurname
        txfPhone.text = mUser?.mPhone
        txfBirth.text = mUser?.mBirth
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    @IBAction func touchModify(_ sender: Any) {
        mUser?.mName = txfName.text
        mUser?.mSurname = txfSurname.text
        mUser?.mPhone = txfPhone.text
        mUser?.mBirth = txfBirth.text
        if (db.update(user: mUser!, current: UserDbManager.STATUS.CURRENT)){
            UtilProj.backNavigation(view: self)
        } else {
            UtilProj.alertError(view: self)
        }
    }
}
