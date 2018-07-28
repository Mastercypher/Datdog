
//
//  ViewVaccinationController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 27/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit

class ViewVaccinationController: UIViewController {
    
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var lblStatus: UILabel!
    @IBOutlet weak var lblWhen: UILabel!
    @IBOutlet weak var viewStatus: UIView!
    @IBOutlet weak var btnCompleted: UIButton!
    
    var mVacc: Vaccination?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.prefersLargeTitles = true
    }
    
    override func viewWillAppear(_ animated: Bool) {
        let active = mVacc?.mDateCompleted == ""
        lblName.text = mVacc?.mName
        lblWhen.text = mVacc?.mDateWhen
        if active {
            lblStatus.text = "Active"
            viewStatus.backgroundColor = UtilProj.getUIColor(hex: UtilProj.COLORS.WARNING)
            btnCompleted.isHidden = false
        } else {
            lblStatus.text = "Done"
            viewStatus.backgroundColor = UtilProj.getUIColor(hex: UtilProj.COLORS.SUCCESS)
            btnCompleted.isHidden = true
        }
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        let destination = segue.destination as? ModifyVaccinationController
        destination?.mVacc = mVacc
    }
    @IBAction func touchDelete(_ sender: Any) {
        let view = self
        if let vacc = mVacc {
            UtilProj.showAlertOk(view: view, title: "Attention", message: "You are deleting a vaccination", handler: { _ in
                let res = VaccDbManager().delete(vacc: vacc)
                if res {
                    UtilProj.backNavigation(view: view)
                } else {
                    UtilProj.alertError(view: view)
                }
            })
        } else {
            UtilProj.alertError(view: self)
        }
    }
    
    @IBAction func touchCompleted(_ sender: Any) {
        if let vacc = mVacc {
            vacc.mDateCompleted = UtilProj.getDateNow()
            let res = VaccDbManager().update(vacc: vacc)
            if res {
                self.viewWillAppear(false)
            } else {
                UtilProj.alertError(view: self)
            }
        } else {
            UtilProj.alertError(view: self)
        }
        
    }
    
}
