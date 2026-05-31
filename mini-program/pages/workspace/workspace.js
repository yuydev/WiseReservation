const app = getApp()

Page({
  data: {
    currentRole: '管理',
    roles: ['管理', '司机', '安保', '调度', '事业部'],
    unreadCount: 3,
    todayThroughput: '1,284.5',
    activeVehicles: 12,
    totalVehicles: 16,
    capabilities: [
      { id: 'digital-twin', name: '数字孪生', icon: '🔮', bgColor: '#7B68EE' },
      { id: 'supplier-reservation', name: '供应商预约', icon: '📅', bgColor: '#4CAF50' },
      { id: 'vehicle-dispatch', name: '车辆调度', icon: '🚛', bgColor: '#FF9800' },
      { id: 'order-management', name: '订单管理', icon: '📋', bgColor: '#2196F3' },
      { id: 'entry-approval', name: '入场审批', icon: '📝', bgColor: '#4CAF50' },
      { id: 'ehs-management', name: 'EHS管理', icon: '🛡️', bgColor: '#009688' },
      { id: 'logistics-tracking', name: '物流跟踪', icon: '📦', bgColor: '#607D8B' },
      { id: 'order-hall', name: '抢单大厅', icon: '🏪', bgColor: '#FF5722' }
    ],
    alerts: [
      {
        id: 1,
        icon: '🚨',
        title: '车辆异常停留',
        description: '苏A·88888 在 A3 道口停留超过 45 分钟',
        time: '5m',
        type: 'warning'
      },
      {
        id: 2,
        icon: '🕐',
        title: '送货预约待批',
        description: '华安建设集团 - 消防系统配件入库',
        time: '12m',
        type: 'pending'
      }
    ]
  },

  onLoad() {
    this.fetchDashboardData()
  },

  onPullDownRefresh() {
    this.fetchDashboardData()
    wx.stopPullDownRefresh()
  },

  fetchDashboardData() {
    const token = wx.getStorageSync('token')
    wx.request({
      url: `${app.globalData.baseUrl}/api/dashboard/overview`,
      method: 'GET',
      header: { 'Authorization': 'Bearer ' + token },
      success: (res) => {
        if (res.data.code === 0) {
          const data = res.data.data
          this.setData({
            todayThroughput: data.todayThroughput,
            activeVehicles: data.activeVehicles,
            totalVehicles: data.totalVehicles,
            alerts: data.alerts || this.data.alerts
          })
        }
      }
    })
  },

  switchRole(e) {
    const role = e.currentTarget.dataset.role
    this.setData({ currentRole: role })
    app.globalData.currentRole = role
    this.fetchDashboardData()
  },

  goNotifications() {
    wx.navigateTo({ url: '/pages/notifications/notifications' })
  },

  goCapability(e) {
    const id = e.currentTarget.dataset.id
    const routeMap = {
      'digital-twin': '/pages/digitalTwin/digitalTwin',
      'supplier-reservation': '/pages/reservations/reservations',
      'vehicle-dispatch': '/pages/vehicleDispatch/vehicleDispatch',
      'order-management': '/pages/orderManagement/orderManagement',
      'entry-approval': '/pages/entryApproval/entryApproval',
      'ehs-management': '/pages/ehsManagement/ehsManagement',
      'logistics-tracking': '/pages/logisticsTracking/logisticsTracking',
      'order-hall': '/pages/orderHall/orderHall'
    }
    const url = routeMap[id]
    if (url) {
      if (id === 'order-hall') {
        wx.switchTab({ url })
      } else {
        wx.navigateTo({ url })
      }
    }
  },

  goAlerts() {
    wx.navigateTo({ url: '/pages/alerts/alerts' })
  },

  goAlertDetail(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({ url: `/pages/alertDetail/alertDetail?id=${id}` })
  }
})
