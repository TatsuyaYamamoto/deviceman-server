var gulp = require('gulp');
var webpack = require("gulp-webpack");
var webpackConf = require('./webpack.config.js');

gulp.task('default', ['webpack', 'watch']);

gulp.task('watch', function() {
    gulp.watch('./src/main/resources/static/**/*.jsx', ['webpack'])
});

gulp.task('webpack',function() {
    gulp.src('main.coffee') //エントリーポイントはmain.coffeeです
        .pipe(webpack(webpackConf)) //gulp-webpackを実行します。
        .pipe(gulp.dest(webpackConf.output.path))
});