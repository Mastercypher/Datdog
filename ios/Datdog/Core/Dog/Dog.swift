//
//  Dog.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 28/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

class Dog: Hashable{
    var hashValue: Int {
        return mId.hashValue
    }
    
    var mId: String!
    var mIdNfc: String!
    var mIdUser: Int!
    var mName: String!
    var mBreed: String!
    var mColour: String!
    var mBirth: String!
    var mSize: String!
    var mSex: String!
    var mDateCreate: String!
    var mDateUpdate: String!
    var mDevare: Int!
    
    /*
    init(id: Int, name: String, surname: String, phone: String, birth: String, email: String,
         password: String, dateCreate: String, dateUpdate: String, devare: Int) {
        mId = id
        mIdNfc = idNfc
        mIdUser = surname
        mPhone = phone
        mBirth = birth
        mEmail = email
        mPassword = password
        mDateCreate = dateCreate
        mDateUpdate = dateUpdate
        mDevare = devare
    }
    */
    static func == (lDog: Dog, rDog: Dog) -> Bool {
        return lDog.mId == rDog.mId
    }
}
