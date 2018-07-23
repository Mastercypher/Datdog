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
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.prefersLargeTitles = true
        viewReport.layer.shadowColor = UIColor.lightGray.cgColor
        viewReport.layer.shadowOpacity = 15
        viewReport.layer.shadowOffset = CGSize(width: -2, height: 10)
        viewReport.layer.shadowRadius = 10
        
        
        
        
        
        // screen width and height:
        let width = viewReport.frame.size.width
        let height = viewReport.frame.size.height
        print(width, height)
        let image = UIImage(named: "image_report")
        let imageView = UIImageView(image: image!)
        imageView.frame = CGRect(x: 0, y: 0, width: width, height: height)

        // you can change the content mode:
        imageView.contentMode = UIViewContentMode.scaleAspectFill
        imageView.clipsToBounds = true
        imageView.layer.cornerRadius = 10;
        viewReport.addSubview(imageView)
        viewReport.sendSubview(toBack: imageView)


    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
}

