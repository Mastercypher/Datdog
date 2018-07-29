//
//  ModifyDogController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 27/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit

class ModifyDogController: UIViewController {
    
    @IBOutlet weak var txfName: UITextField!
    @IBOutlet weak var txfBreed: UITextField!
    @IBOutlet weak var txfColour: UITextField!
    @IBOutlet weak var txfBirth: UITextField!
    @IBOutlet weak var sgmSex: UISegmentedControl!
    @IBOutlet weak var sgmSize: UISegmentedControl!
    
    let mDbDog = DogDbManager()
    var mDog: Dog?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.prefersLargeTitles = true
    }
    
    override func viewWillAppear(_ animated: Bool) {
        mDog = mDbDog.getById(id: (mDog?.mId)!)
        txfName.text = mDog?.mName
        txfBreed.text = mDog?.mBreed
        txfColour.text = mDog?.mColour
        txfBirth.text = mDog?.mBirth
        sgmSex.selectedSegmentIndex = (mDog?.mSex)!
        sgmSize.selectedSegmentIndex = (mDog?.mSize)!
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    @IBAction func touchModify(_ sender: Any) {
        var errorMessage = ""
        if let dog = mDog?.clone() {
            dog.mName = txfName.text
            dog.mBreed = txfBreed.text
            dog.mColour = txfColour.text
            dog.mBirth = txfBirth.text
            dog.mSex = sgmSex.selectedSegmentIndex
            dog.mSize = sgmSize.selectedSegmentIndex
            
            // Check if all the field are more of one character
            if (dog.mName.count > 0 && dog.mBreed.count > 0 && dog.mColour.count > 0 && dog.mBirth.count > 0)  {
                // Check if all the field are less of 50 characters
                if (dog.mName.count < 50 && dog.mBreed.count < 50 && dog.mColour.count < 50 && dog.mBirth.count < 50)  {
                    
                    let success = mDbDog.update(dog: dog)
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
