//
//  ViewController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 22/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit
import QuartzCore

class ReportsController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    @IBOutlet weak var btnAddReport: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.prefersLargeTitles = true
        btnAddReport.layer.cornerRadius = 0.5 * btnAddReport.bounds.size.width;
        btnAddReport.imageEdgeInsets = UIEdgeInsetsMake(20, 20, 20, 20)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        //return continenti.count
        return 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cella = tableView.dequeueReusableCell(withIdentifier: "cellReport", for: indexPath)
        //let stato = continenti[indexPath.row]
        //cella.imageView?.image = UIImage(named: stato.lowercased())
        //cella.textLabel?.text = stato
        return cella
 
        
    }
}

