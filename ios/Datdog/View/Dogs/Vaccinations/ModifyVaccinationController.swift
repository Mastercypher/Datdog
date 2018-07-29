//
//  ModifyVaccinationController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 27/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit

class ModifyVaccinationController: UIViewController {
    
    @IBOutlet weak var txfName: UITextField!
    @IBOutlet weak var txfWhen: UITextField!
    
    var mVacc: Vaccination?
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.prefersLargeTitles = true
    }
    override func viewWillAppear(_ animated: Bool) {
        mVacc = VaccDbManager().getById(id: (mVacc?.mId)!)
        if let vacc = mVacc {
            txfName.text = vacc.mName
            txfWhen.text = vacc.mDateWhen
        }
        super.viewWillAppear(animated)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    @IBAction func touchModify(_ sender: Any) {
        var errorMessage = ""
        let dbVacc = VaccDbManager()
        if let vacc = mVacc {
            vacc.mName = txfName.text
            vacc.mDateWhen = txfWhen.text
            
            // Check if all the field are more of one character
            if (vacc.mName.count > 0 && vacc.mDateWhen.count > 0)  {
                // Check if all the field are less of 50 characters
                if (vacc.mName.count < 50 && vacc.mDateWhen.count < 50)  {
                    let success = dbVacc.update(vacc: vacc)
                    if success {
                        UtilProj.backNavigation(view: self)
                    } else {
                        errorMessage = UtilProj.ERR.SAVING
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
    }
}
