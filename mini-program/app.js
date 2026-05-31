App({
  onLaunch() {
    // 登录
    wx.login({
      success: (res) => {
        if (res.code) {
          // 发送 res.code 到后台换取 openId, sessionKey, unionId
          wx.request({
            url: `${this.globalData.baseUrl}/api/auth/login`,
            method: 'POST',
            data: { code: res.code },
            success: (response) => {
              if (response.data.code === 0) {
                this.globalData.token = response.data.data.token
                this.globalData.userInfo = response.data.data.userInfo
                wx.setStorageSync('token', response.data.data.token)
              }
            }
          })
        }
      }
    })
  },
  globalData: {
    baseUrl: 'http://localhost:8080',
    token: '',
    userInfo: null,
    currentRole: '管理'
  }
})
