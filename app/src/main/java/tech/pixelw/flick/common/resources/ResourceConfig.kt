package tech.pixelw.flick.common.resources

/**
 *
 * NOTE: 关于资源
 *
 * 在此处集中管理各业务块资源来源;
 *
 * 由于未来有兼容其他api的需要, 大部分api不会直接include资源的URL, 需要手动构造;
 *
 * feature中不能硬编码完整URL;
 *
 */
object ResourceConfig {

    const val LAPI = 1

    // TODO: SLY 24/2/19 动态配置多个资源源站

    var netApiRoot = Urls.lapiRootPath

    var musicFile = LAPI
        private set

    var musicArtImg = LAPI
        private set

    var cardArtImg = LAPI
        private set

    var cardThumbImg = LAPI
        private set


}