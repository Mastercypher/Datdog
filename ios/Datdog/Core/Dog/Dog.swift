//
//  Dog.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 28/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

class Dog{
    
    static let SEX_M = 0
    static let SEX_F = 1
    static let SIZE_SMALL = 0
    static let SIZE_BIG = 1
    
    var mId: String!
    var mIdNfc: String!
    var mIdUser: Int!
    var mName: String!
    var mBreed: String!
    var mColour: String!
    var mBirth: String!
    var mSize: Int!
    var mSex: Int!
    var mDateCreate: String!
    var mDateUpdate: String!
    var mDelete: Int!
    
    // Init with less params
    init(idUser: Int, name: String, breed: String, colour: String,
         birth: String, size: Int, sex: Int) {
        let dateNow = UtilProj.getDateNow()
        
        mId = self.createId(idUser: idUser, name: name, dateNow: dateNow)
        mIdNfc = ""
        mIdUser = idUser
        mName = name
        mBreed = breed
        mColour = colour
        mBirth = birth
        mSize = size
        mSex = sex
        mDateCreate = dateNow
        mDateUpdate = dateNow
        mDelete = 0
    }
    
    // Init with more params
    init(id: String, idNfc: String, idUser: Int, name: String, breed: String, colour: String,
         birth: String, size: Int, sex: Int, dateCreate: String, dateUpdate: String, delete: Int) {
        mId = id
        mIdNfc = idNfc
        mIdUser = idUser
        mName = name
        mBreed = breed
        mColour = colour
        mBirth = birth
        mSize = size
        mSex = sex
        mDateCreate = dateCreate
        mDateUpdate = dateUpdate
        mDelete = delete
    }
    
    func createId(idUser: Int, name: String, dateNow: String) -> String{
        return String(idUser) + "-" + name + "-" + dateNow
    }
    
    static func == (lDog: Dog, rDog: Dog) -> Bool {
        return lDog.mId == rDog.mId
    }
}
