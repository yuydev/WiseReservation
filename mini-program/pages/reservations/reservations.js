const app = getApp()

Page({
  data: {
    searchKey: '',
    currentStatus: 'all',
    statusList: [
      { label: '全部', value: 'all', count: 0 },
      { label: '待审批', value: 'pending', count: 3 },
      { label: '已通过', value: 'approved', count: 0 },
      { label: '已拒绝', value: 'rejected', count: 0 },
      { label: '已完成', value: 'completed', count: 0 },
      { label: '已取消', value: 'cancelled', count: 0 }
    ],
    reservations: [
      {
        id: 1,
        orderNo: 'YY20260531001',
        supplierName: '华安建设集团',
        materialName: '消防系统配件',
        appointmentTime: '2026-05-31 14:00',
        plateNumber: '苏A·88888',
        status: 'pending',
        statusText: '待审批',
        createTime: '2026-05-31 10:30'
      },
      {
        id: 2,
        orderNo: 'YY20260531002',
        supplierName: '江苏建材有限公司',
        materialName: '钢筋 HRB400',
        appointmentTime: '2026-05-31 15:30',
        plateNumber: '苏B·66666',
        status: 'approved',
        statusText: '已通过',
        createTime: '2026-05-31 09:15'
      },
      {
        id: 3,
        orderNo: 'YY20260530003',
        supplierName: '南京混凝土公司',
        materialName: 'C30混凝土',
        appointmentTime: '2026-05-30 08:00',
        plateNumber: '苏A·12345',
        status: 'completed',
        statusText: '已完成',
        createTime: '2026-05-29 16:00'
      }
    ],
    loading: false,
    isRefreshing: false,
    page: 1,
    hasMore: true
  },

  onLoad() {
    this.fetchReservations()
  },

  fetchReservations(isLoadMore = false) {
    if (this.data.loading) return
    this.setData({ loading: true })

    const token = wx.getStorageSync('token')
    wx.request({
      url: `${app.globalData.baseUrl}/api/reservations`,
      method: 'GET',
      header: { 'Authorization': `****** },
      data: {
        status: this.data.currentStatus === 'all' ? '' : this.data.currentStatus,
        keyword: this.data.searchKey,
        page: this.data.page,
        size: 10
      },
      success: (res) => {
        if (res.data.code === 0) {
          const list = res.data.data.records || []
          this.setData({
            reservations: isLoadMore ? [...this.data.reservations, ...list] : list,
            hasMore: list.length === 10
          })
        }
      },
      complete: () => {
        this.setData({ loading: false, isRefreshing: false })
      }
    })
  },

  onSearch(e) {
    this.setData({ searchKey: e.detail.value, page: 1 })
    this.fetchReservations()
  },

  switchStatus(e) {
    const status = e.currentTarget.dataset.value
    this.setData({ currentStatus: status, page: 1 })
    this.fetchReservations()
  },

  loadMore() {
    if (!this.data.hasMore || this.data.loading) return
    this.setData({ page: this.data.page + 1 })
    this.fetchReservations(true)
  },

  onRefresh() {
    this.setData({ isRefreshing: true, page: 1 })
    this.fetchReservations()
  },

  goDetail(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({ url: `/pages/reservationDetail/reservationDetail?id=${id}` })
  },

  createReservation() {
    wx.navigateTo({ url: '/pages/createReservation/createReservation' })
  },

  cancelReservation(e) {
    const id = e.currentTarget.dataset.id
    wx.showModal({
      title: '确认取消',
      content: '确定要取消该预约单吗？',
      success: (res) => {
        if (res.confirm) {
          this.updateReservationStatus(id, 'cancelled')
        }
      }
    })
  },

  approveReservation(e) {
    const id = e.currentTarget.dataset.id
    wx.showModal({
      title: '确认审批',
      content: '确定通过该预约申请吗？',
      success: (res) => {
        if (res.confirm) {
          this.updateReservationStatus(id, 'approved')
        }
      }
    })
  },

  updateReservationStatus(id, status) {
    const token = wx.getStorageSync('token')
    wx.request({
      url: `${app.globalData.baseUrl}/api/reservations/${id}/status`,
      method: 'PUT',
      header: { 'Authorization': `****** },
      data: { status },
      success: (res) => {
        if (res.data.code === 0) {
          wx.showToast({ title: '操作成功', icon: 'success' })
          this.setData({ page: 1 })
          this.fetchReservations()
        }
      }
    })
  }
})
