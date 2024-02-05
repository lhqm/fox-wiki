
// 官方配置文档：https://webpack.js.org/configuration/dev-server/
module.exports = {
	devServer: {
		hot: true,
		disableHostCheck: true,
		port: 80,
		host: '127.0.0.1'
	},
	publicPath: './',
	outputDir: '../../zyplayer-doc-manage/src/main/resources/dist',
	productionSourceMap: false,
	pages: {
		index: {
			entry: 'src/main.js',
			template: 'public/index.html',
			filename: process.env.NODE_ENV === 'production'?'doc-console.html':'index.html',
		},
	}
};
