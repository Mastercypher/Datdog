//
//  AddDogController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 27/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit

class AddDogController: UIViewController {
    
    @IBOutlet weak var txfName: UITextField!
    @IBOutlet weak var txfBreed: UITextField!
    @IBOutlet weak var txfColour: UITextField!
    @IBOutlet weak var txfBirth: UITextField!
    @IBOutlet weak var sgmSex: UISegmentedControl!
    @IBOutlet weak var sgmSize: UISegmentedControl!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.prefersLargeTitles = true
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    @IBAction func touchAdd(_ sender: Any) {
        var errorMessage = ""
        // DB var
        let dbUser = UserDbManager()
        let dbDog = DogDbManager()
        let user = dbUser.getCurrent(view: self)
        // TextField var
        let name = txfName.text!
        let breed = txfBreed.text!
        let colour = txfColour.text!
        let birth = txfBirth.text!
        let sex = sgmSex.selectedSegmentIndex
        let size = sgmSize.selectedSegmentIndex
        
        // Check if all the field are more of one character
        if (name.count > 0 && breed.count > 0 && colour.count > 0 && birth.count > 0)  {
            // Check if all the field are less of 50 characters
            if (name.count < 50 && breed.count < 50 && colour.count < 50 && birth.count < 50)  {
                let success = dbDog.insert(dog: Dog(idUser: (user?.mId)!, name: name, breed: breed, colour: colour,
                                                    birth: birth, size: size, sex: sex))
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
