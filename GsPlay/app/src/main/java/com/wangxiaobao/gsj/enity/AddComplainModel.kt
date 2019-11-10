package com.wangxiaobao.gsj.enity

data class AddComplainModel(var complaintTypeName: String,
                            var content: String,
                            var mobile: String,
                            var sex: Int,
                            var userName: String,
                            var age: String,
                            var merchantId: String,
                            var voiceUrl: String?)