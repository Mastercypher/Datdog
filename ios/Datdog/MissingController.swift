//
//  ViewController.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 22/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import UIKit
import QuartzCore

class MissingController: UIViewController {
    
    @IBOutlet weak var viewReport: UIView!
    @IBOutlet weak var viewLost: UIView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.prefersLargeTitles = true
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        viewReport.layer.shadowColor = UIColor.lightGray.cgColor
        viewReport.layer.shadowOpacity = 15
        viewReport.layer.shadowOffset = CGSize(width: -2, height: 10)
        viewReport.layer.shadowRadius = 10
        
        // Set the image for the REPORT view
        let widthR = viewReport.frame.size.width
        let heightR = viewReport.frame.size.height
        let imgReport = UIImage(named: "image_dog_face")
        let imvReport = UIImageView(image: imgReport!)
        imvReport.frame = CGRect(x: 0, y: 0, width: widthR, height: heightR)
        imvReport.contentMode = UIViewContentMode.scaleAspectFill
        imvReport.clipsToBounds = true
        imvReport.layer.cornerRadius = 10;
        viewReport.addSubview(imvReport)
        viewReport.sendSubview(toBack: imvReport)
        
        // Set the image for the LOST view
        let widthL = viewLost.frame.size.width
        let heightL = viewLost.frame.size.height
        let imgLost = UIImage(named: "image_dog_car")
        let imvLost = UIImageView(image: imgLost!)
        imvLost.frame = CGRect(x: 0, y: 0, width: widthL, height: heightL)
        imvLost.contentMode = UIViewContentMode.scaleAspectFill
        imvLost.clipsToBounds = true
        imvLost.layer.cornerRadius = 10;
        viewLost.addSubview(imvLost)
        viewLost.sendSubview(toBack: imvLost)
        
    }
}

