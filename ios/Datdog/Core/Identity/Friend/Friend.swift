//
//  Friend.swift
//  Datdog
//
//  Created by Alessandro Riccardi on 28/07/2018.
//  Copyright © 2018 Mastercypher. All rights reserved.
//

class Friend {
    
    var mId: String!
    var mIdUser: Int!
    var mIdFriend: Int!
    var mDateCreate: String!
    var mDateUpdate: String!
    var mDelete: Int!
    
    // Init with less params
    init(idUser: Int, idFriend: Int) {
        let dateNow = UtilProj.getDateNow()
        
        mId = self.createId(idUser: idUser, idFriend: idFriend, dateNow: dateNow)
        mIdUser = idUser
        mIdFriend = idFriend
        mDateCreate = dateNow
        mDateUpdate = dateNow
        mDelete = UtilProj.DBSTATUS.AVAILABLE
    }
    
    // Init with more params
    init(id: String, idUser: Int, idFriend: Int, dateCreate: String, dateUpdate: String, delete: Int) {
        mId = id
        mIdUser = idUser
        mIdFriend = idFriend
        mDateCreate = dateCreate
        mDateUpdate = dateUpdate
        mDelete = delete
    }
    
    func createId(idUser: Int, idFriend: Int, dateNow: String) -> String{
        return String(idUser) + "-" + String(idFriend) + "-" + dateNow
    }
    
    static func == (lFriend: Friend, rFriend: Friend) -> Bool {
        return lFriend.mId == rFriend.mId
    }
}

