//
//  Date.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 26/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import Foundation

class DateProj {
    static func getDateNow() -> String{
        let date = Date()
        let formatter = DateFormatter()
        formatter.dateFormat = "dd/MM/yyyy-HH:mm:ss"
        return formatter.string(from: date)
    }
}
