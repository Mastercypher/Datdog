//
//  AddVaccinationController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 27/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit

class AddVaccinationController: UIViewController {
    
    @IBOutlet weak var txfName: UITextField!
    @IBOutlet weak var txfWhen: UITextField!
    
    var mDbVacc = VaccDbManager()
    var mDog: Dog?
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.prefersLargeTitles = true
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    @IBAction func touchAdd(_ sender: Any) {
        var errorMessage = ""
        // DB var
        // TextField var
        let name = txfName.text!
        let when = txfWhen.text!
        
        // Check if all the field are more of one character
        if (name.count > 0 && when.count > 0 )  {
            // Check if all the field are less of 50 characters
            if (name.count < 50 && when.count < 50)  {
                let success = mDbVacc.insert(vacc: Vaccination(idDog: (mDog?.mId)!, name: name, dateWhen: when))
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
