//
//  ViewController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 22/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit
import QuartzCore

class MenuController: UIViewController {
    
    @IBOutlet weak var btnAddDog: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.prefersLargeTitles = true
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    @IBAction func goMissing(_ sender: Any) {
        self.tabBarController?.selectedIndex = 1;
    }
    @IBAction func goFriends(_ sender: Any) {
        self.tabBarController?.selectedIndex = 2;
    }
    @IBAction func goConnect(_ sender: Any) {
        self.tabBarController?.selectedIndex = 3;
    }
    @IBAction func goYourDogs(_ sender: Any) {
        self.tabBarController?.selectedIndex = 4;
    }
}

