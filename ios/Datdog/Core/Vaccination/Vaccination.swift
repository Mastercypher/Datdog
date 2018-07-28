//
//  Dog.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 28/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

class Vaccination{
    
    var mId: String!
    var mIdDog: String!
    var mName: String!
    var mDateWhen: String!
    var mDateCreate: String!
    var mDateUpdate: String!
    var mDateCompleted: String!
    var mDelete: Int!
    
    // Init with less params
    init(idDog: String, name: String, dateWhen: String) {
        let dateNow = UtilProj.getDateNow()
        
        mId = self.createId(idDog: idDog, name: name, dateNow: dateNow)
        mIdDog = idDog
        mName = name
        mDateWhen = dateWhen
        mDateCreate = dateNow
        mDateUpdate = dateNow
        mDateCompleted = ""
        mDelete = UtilProj.DBSTATUS.AVAILABLE
    }
    
    // Init with more params
    init(id: String, idDog: String, name: String, dateWhen: String, dateCreate: String, dateUpdate: String,
         dateCompleted: String, delete: Int) {
        mId = id
        mIdDog = idDog
        mName = name
        mDateWhen = dateWhen
        mDateCreate = dateCreate
        mDateUpdate = dateUpdate
        mDateCompleted = dateCompleted
        mDelete = UtilProj.DBSTATUS.AVAILABLE
    }
    
    func createId(idDog: String, name: String, dateNow: String) -> String{
        return String(idDog) + "-" + name + "-" + dateNow
    }
    
    static func == (lVacc: Vaccination, rVacc: Vaccination) -> Bool {
        return lVacc.mId == rVacc.mId
    }
}
