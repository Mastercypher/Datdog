//
//  ViewController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 22/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit
import QuartzCore

class YourDogsController: UIViewController {
    
    @IBOutlet weak var btnAddDog: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.prefersLargeTitles = true
        btnAddDog.layer.cornerRadius = 0.5 * btnAddDog.bounds.size.width;
        btnAddDog.imageEdgeInsets = UIEdgeInsetsMake(20, 20, 20, 20)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
}

