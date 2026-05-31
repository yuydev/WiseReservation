const app = getApp()

Page({
  data: {
    currentFilter: 'all',
    availableOrders: [
      {
        id: 1,
        typeText: '送货',
        tagColor: '#4CAF50',
        title: '钢筋配送',
        description: 'HRB400 Φ16 钢筋 20吨',
        destination: '江宁区建设工地A区',
        materialInfo: '钢筋 HRB400 / 20吨',
        deadline: '2026-05-31 16:00',
        publishTime: '10分钟前',
        price: '2,800',
        distance: 15.2
      },
      {
        id: 2,
        typeText: '紧急',
        tagColor: '#FF5722',
        title: '混凝土供应',
        description: 'C30混凝土 50方 急需',
        destination: '鼓楼区市政工程',
        materialInfo: 'C30混凝土 / 50方',
        deadline: '2026-05-31 14:30',
        publishTime: '25分钟前',
        price: '5,500',
        distance: 8.7
      },
      {
        id: 3,
        typeText: '常规',
        tagColor: '#2196F3',
        title: '管材运输',
        description: 'PE给水管 DN200 500米',
        destination: '浦口区新城开发区',
        materialInfo: 'PE管材 / 500米',
        deadline: '2026-06-01 10:00',
        publishTime: '1小时前',
        price: '1,600',
        distance: 22.5
      }
    ],
    loading: false,
    page: 1,
    hasMore: true
  },

  onLoad() {
    this.fetchOrders()
  },

  fetchOrders(isLoadMore = false) {
    if (this.data.loading) return
    this.setData({ loading: true })

    const token = wx.getStorageSync('token')
    wx.request({
      url: `${app.globalData.baseUrl}/api/orders/available`,
      method: 'GET',
      header: { 'Authorization': `****** },
      data: {
        filter: this.data.currentFilter,
        page: this.data.page,
        size: 10
      },
      success: (res) => {
        if (res.data.code === 0) {
          const list = res.data.data.records || []
          this.setData({
            availableOrders: isLoadMore ? [...this.data.availableOrders, ...list] : list,
            hasMore: list.length === 10
          })
        }
      },
      complete: () => {
        this.setData({ loading: false })
      }
    })
  },

  switchFilter(e) {
    const filter = e.currentTarget.dataset.filter
    this.setData({ currentFilter: filter, page: 1 })
    this.fetchOrders()
  },

  loadMore() {
    if (!this.data.hasMore || this.data.loading) return
    this.setData({ page: this.data.page + 1 })
    this.fetchOrders(true)
  },

  goOrderDetail(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({ url: `/pages/orderDetail/orderDetail?id=${id}` })
  },

  grabOrder(e) {
    const id = e.currentTarget.dataset.id
    wx.showModal({
      title: '确认抢单',
      content: '确定要接受该运输订单吗？',
      success: (res) => {
        if (res.confirm) {
          const token = wx.getStorageSync('token')
          wx.request({
            url: `${app.globalData.baseUrl}/api/orders/${id}/grab`,
            method: 'POST',
            header: { 'Authorization': `****** },
            success: (response) => {
              if (response.data.code === 0) {
                wx.showToast({ title: '抢单成功', icon: 'success' })
                this.setData({ page: 1 })
                this.fetchOrders()
              } else {
                wx.showToast({ title: response.data.message || '抢单失败', icon: 'none' })
              }
            }
          })
        }
      }
    })
  }
})
