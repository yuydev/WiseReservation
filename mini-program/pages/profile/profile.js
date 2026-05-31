const app = getApp()

Page({
  data: {
    userInfo: {
      name: '张明',
      roleName: '调度管理员',
      company: '智慧物流有限公司',
      avatarUrl: ''
    },
    stats: {
      totalOrders: 128,
      completedOrders: 115,
      rating: '4.9',
      thisMonth: 23
    },
    menuList: [
      { id: 'myOrders', name: '我的订单', icon: '📋', url: '/pages/myOrders/myOrders' },
      { id: 'myReservations', name: '我的预约', icon: '📅', url: '/pages/reservations/reservations' },
      { id: 'myVehicles', name: '我的车辆', icon: '🚛', url: '/pages/myVehicles/myVehicles' },
      { id: 'wallet', name: '我的钱包', icon: '💰', url: '/pages/wallet/wallet' },
      { id: 'certificates', name: '资质证书', icon: '📄', url: '/pages/certificates/certificates' }
    ],
    settingList: [
      { id: 'notifications', name: '消息通知', icon: '🔔', url: '/pages/notificationSettings/notificationSettings' },
      { id: 'help', name: '帮助中心', icon: '❓', url: '/pages/help/help' },
      { id: 'about', name: '关于我们', icon: 'ℹ️', url: '/pages/about/about' },
      { id: 'feedback', name: '意见反馈', icon: '✉️', url: '/pages/feedback/feedback' }
    ]
  },

  onLoad() {
    this.fetchUserInfo()
  },

  onShow() {
    this.fetchUserStats()
  },

  fetchUserInfo() {
    const token = wx.getStorageSync('token')
    if (!token) return
    wx.request({
      url: `${app.globalData.baseUrl}/api/user/profile`,
      method: 'GET',
      header: { 'Authorization': 'Bearer ' + token },
      success: (res) => {
        if (res.data.code === 0) {
          this.setData({ userInfo: res.data.data })
        }
      }
    })
  },

  fetchUserStats() {
    const token = wx.getStorageSync('token')
    if (!token) return
    wx.request({
      url: `${app.globalData.baseUrl}/api/user/stats`,
      method: 'GET',
      header: { 'Authorization': 'Bearer ' + token },
      success: (res) => {
        if (res.data.code === 0) {
          this.setData({ stats: res.data.data })
        }
      }
    })
  },

  goPage(e) {
    const url = e.currentTarget.dataset.url
    wx.navigateTo({ url })
  },

  logout() {
    wx.showModal({
      title: '确认退出',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync('token')
          app.globalData.token = ''
          app.globalData.userInfo = null
          wx.reLaunch({ url: '/pages/workspace/workspace' })
        }
      }
    })
  }
})
