//
//  ViewDogController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 27/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit

class ViewDogController: UIViewController {
    
    @IBOutlet weak var lblTitleName: UILabel!
    @IBOutlet weak var lblBreed: UILabel!
    @IBOutlet weak var lblColour: UILabel!
    @IBOutlet weak var lblBirth: UILabel!
    @IBOutlet weak var lblSex: UILabel!
    @IBOutlet weak var lblSize: UILabel!
    
    var mDog: Dog?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.prefersLargeTitles = true
    }
    
    override func viewWillAppear(_ animated: Bool) {
        mDog = DogDbManager().getById(id: (mDog?.mId)!)
        lblTitleName.text = mDog?.mName
        lblBreed.text = mDog?.mBreed
        lblColour.text = mDog?.mColour
        lblBirth.text = mDog?.mBirth
        lblSex.text = mDog?.mSex == Dog.SEX_M ? "Male" : "Famele"
        lblSize.text = mDog?.mSize == Dog.SIZE_SMALL ? "Small" : "Big"
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    @IBAction func touchRemove(_ sender: Any) {
        let view = self
        if let dog = mDog {
            UtilProj.showAlertOk(view: view, title: "Attention", message: "You are deleting a dog", handler: { _ in
                let res = DogDbManager().delete(dog: dog)
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
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        switch segue.identifier {
        case "toModify":
            let destination = segue.destination as! ModifyDogController
            destination.mDog = mDog
            
        case "toVaccinations":
            let destination = segue.destination as! VaccinationsController
            destination.mDog = mDog
            
        default: break
        }
    }
}
