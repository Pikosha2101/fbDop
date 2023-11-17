package com.example.fbdop

interface Listener<T: IParam> {
    fun onClick1(param: T)
    fun onClick2(param: T)
}
interface IParam