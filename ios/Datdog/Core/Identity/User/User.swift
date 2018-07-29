//
//  User.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 26/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

import Foundation

class User: Hashable{
    var hashValue: Int {
        return mId.hashValue
    }
    
    var mId: Int!
    var mName: String!
    var mSurname: String!
    var mPhone: String!
    var mBirth: String!
    var mEmail: String!
    var mPassword: String!
    var mDateCreate: String!
    var mDateUpdate: String!
    var mDelete: Int!
    
    init(id: Int, name: String, surname: String, phone: String, birth: String, email: String,
         password: String, dateCreate: String, dateUpdate: String, delete: Int) {
        mId = id
        mName = name
        mSurname = surname
        mPhone = phone
        mBirth = birth
        mEmail = email
        mPassword = password
        mDateCreate = dateCreate
        mDateUpdate = dateUpdate
        mDelete = delete
    }
    
    static func == (lUser: User, rUser: User) -> Bool {
        return lUser.mId == rUser.mId
    }
}
