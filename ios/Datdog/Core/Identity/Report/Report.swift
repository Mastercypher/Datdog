//
//  Report.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 28/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

class Report {
    
    var mId: String!
    var mIdUser: Int!
    var mIdDog: String!
    var mLocation: String!
    var mDateCreate: String!
    var mDateUpdate: String!
    var mDateFound: String!
    var mDelete: Int!
    
    // Init with less params
    init(idUser: Int, idDog: String, location: String) {
        let dateNow = UtilProj.getDateNow()
        
        mId = self.createId(idDog: idDog, idUser: idUser, dateNow: dateNow)
        mIdUser = idUser
        mIdDog = idDog
        mLocation = location
        mDateCreate = dateNow
        mDateUpdate = dateNow
        mDateFound = ""
        mDelete = UtilProj.DBSTATUS.AVAILABLE
    }
    
    // Init with more params
    init(id: String, idUser: Int, idDog: String, location: String, dateCreate: String, dateUpdate: String,
         dateFound: String, delete: Int) {
        mId = id
        mIdDog = idDog
        mLocation = location
        mDateCreate = dateCreate
        mDateUpdate = dateUpdate
        mDateFound = dateFound
        mDelete = UtilProj.DBSTATUS.AVAILABLE
    }
    
    func createId(idDog: String, idUser: Int, dateNow: String) -> String{
        return idDog + "-" + String(idUser) + "-" + dateNow
    }
    
    static func == (lReport: Report, rReport: Report) -> Bool {
        return lReport.mId == rReport.mId
    }
}

