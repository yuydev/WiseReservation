const app = getApp()

/**
 * 封装请求方法
 */
const request = (options) => {
  return new Promise((resolve, reject) => {
    const token = wx.getStorageSync('token')
    wx.request({
      url: `${app.globalData.baseUrl}${options.url}`,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        'Authorization': token ? `****** : '',
        ...options.header
      },
      success: (res) => {
        if (res.statusCode === 401) {
          wx.removeStorageSync('token')
          wx.reLaunch({ url: '/pages/workspace/workspace' })
          reject(new Error('未授权'))
          return
        }
        if (res.data.code === 0) {
          resolve(res.data.data)
        } else {
          wx.showToast({ title: res.data.message || '请求失败', icon: 'none' })
          reject(new Error(res.data.message))
        }
      },
      fail: (err) => {
        wx.showToast({ title: '网络错误', icon: 'none' })
        reject(err)
      }
    })
  })
}

/**
 * 格式化时间
 */
const formatTime = (date) => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()
  return `${[year, month, day].map(formatNumber).join('-')} ${[hour, minute, second].map(formatNumber).join(':')}`
}

const formatNumber = (n) => {
  n = n.toString()
  return n[1] ? n : `0${n}`
}

/**
 * 相对时间
 */
const relativeTime = (timestamp) => {
  const now = Date.now()
  const diff = now - timestamp
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)

  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (days < 30) return `${days}天前`
  return formatTime(new Date(timestamp))
}

module.exports = {
  request,
  formatTime,
  formatNumber,
  relativeTime
}
