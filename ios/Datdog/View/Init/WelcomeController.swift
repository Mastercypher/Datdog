//
//  ViewController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 22/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit

class WelcomeController: UIViewController {

    let db = UserDbManager()
    var mUser: User? = nil

    override func loadView() {
        mUser = db.getCurrent(view: self)
        if (mUser != nil) {
            self.goToMainNavigation()
        } else {
            super.loadView()
        }
        /*
        let defaults = UserDefaults.standard
        let welcomeDid = defaults.bool(forKey: "welcomeDid")
        if welcomeDid {
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "loginView") as! LoginController
            self.present(vc, animated: true, completion:nil )
        } else {
            defaults.set(true, forKey: "welcomeDid")
            super.loadView()
        }
        */
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.prefersLargeTitles = true
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    override func viewWillAppear(_ animated: Bool) {
        self.navigationController?.setNavigationBarHidden(true, animated: animated)
        super.viewWillAppear(animated)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        self.navigationController?.setNavigationBarHidden(false, animated: animated)
        super.viewWillDisappear(animated)
    }
    
    func goToMainNavigation(){
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "mainNav") as! UITabBarController
        self.present(vc, animated: false, completion:nil )
    }
}

