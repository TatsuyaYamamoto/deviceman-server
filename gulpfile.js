var gulp = require('gulp');
var uglify = require('gulp-uglify');
var stripDebug = require('gulp-strip-debug');

var webpack = require("gulp-webpack");
var webserver   = require('gulp-webserver');
var webpackConf = require('./webpack.config.js');

var config = {
    watchTarget:        './src-client/js/**/*.js',
    webServerRootDir:   './src/main/resources/templates/',
    webserverOpts: {
        host: 'localhost',
        port: 8001,
        livereload: false
    }
};

gulp.task('default', ['webpack', 'watch']);

gulp.task('minify', ()=>{
    gulp.src('index.js')
        .pipe(webpack(webpackConf))
        .pipe(stripDebug())
        .pipe(uglify())
        .pipe(gulp.dest(webpackConf.output.path))
});

gulp.task('watch', function() {
    gulp.watch(config.watchTarget, ['webpack'])
});

gulp.task('webserver', function() {
    gulp.src(config.webServerRootDir)
        .pipe(webserver(config.webserverOpts));
});

gulp.task('webpack',function() {
    gulp.src('index.js')
        .pipe(webpack(webpackConf))
        .pipe(gulp.dest(webpackConf.output.path))
});