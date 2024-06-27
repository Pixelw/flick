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

    // TODO: SLY 24/2/19 动态配置多个资源源站

    var netApiDefault = PresetHosts.LAPI

    var musicFile = PresetHosts.LAPI
        private set

    var musicArtImg = PresetHosts.LAPI
        private set

    var cardArtImg = PresetHosts.LAPI
        private set

    var cardThumbImg = PresetHosts.LAPI
        private set


    fun applyConfig(resourceHostConfig: ResourceHostConfig) {
        //// TODO: SLY 24/6/27
    }


}