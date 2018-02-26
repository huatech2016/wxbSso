var SsowxbPlugin = function () {
}

SsowxbPlugin.prototype.isPlatformIOS = function () {
    var isPlatformIOS = device.platform == 'iPhone'
        || device.platform == 'iPad'
        || device.platform == 'iPod touch'
        || device.platform == 'iOS'
    return isPlatformIOS
}

SsowxbPlugin.prototype.call_native = function (success, error, name, args) {
    ret = cordova.exec(success, error, 'SsowxbPlugin', name, args)
    return ret
}


SsowxbPlugin.prototype.hasInstalledMiYaoBao = function (success, error) {
    this.call_native(success, error, 'hasInstalledMiYaoBao')
}
SsowxbPlugin.prototype.getToken = function (success, error) {
    this.call_native(success, error, 'getToken')
}
SsowxbPlugin.prototype.saveToken = function (success, error, token) {
    this.call_native(success, error, 'saveToken', [token])
}
SsowxbPlugin.prototype.getCode = function (success, error) {
    this.call_native(success, error, 'getCode')
}
    SsowxbPlugin.prototype.delToken = function (success, error) {
        this.call_native(success, error, 'delToken')
    }


if (!window.plugins) {
    window.plugins = {}
}

if (!window.plugins.ISsowxbPlugin) {
    window.plugins.ISsowxbPlugin = new SsowxbPlugin()
}

module.exports = new SsowxbPlugin()

/**
 * 接口列表
 * 1  hasInstalledMiYaoBao() 是否安装密钥宝 return yes or other
 * 2 读token   getToken()
 * 3 保存  token   saveAccessToken（String token ）
 * 4  向密钥宝请求code  resisterCode()  返回code 成功或者失败
 * 5 向服务器发起登陆请求（ajax）
 */


/**
 * 接口列表
 * 1  hasInstalledMiYaoBao() 是否安装密钥宝 return yes or other
 * 2 读token   getToken()
 * 3 保存  token   saveAccessToken（String token ）
 * 4  向密钥宝请求code  resisterCode()  返回code 成功或者失败
 * 5 向服务器发起登陆请求（ajax）
 */
