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
    @IBOutlet weak var touchAdd: UIButton!
}
