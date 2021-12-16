package com.olamachia.maptrackerweekeighttask.models

class LocationModel {
    var id: String?=null
    var latitude:Double?=null
    var longitude:Double?=null

    constructor() {
    }
    constructor(id: String?, latitude: Double?, longitude: Double) {
        this.latitude= latitude
        this.longitude = longitude
        this.id = id
    }

}