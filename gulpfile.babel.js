import gulp from 'gulp';
import sass from 'gulp-sass';
import browserify from 'browserify';
import source from 'vinyl-source-stream';
import buffer from 'vinyl-buffer';
import eslint from 'gulp-eslint';
import exorcist from 'exorcist';
import browserSync from 'browser-sync';
import watchify from 'watchify';
import babelify from 'babelify';
import uglify from 'gulp-uglify';
import ifElse from 'gulp-if-else';

watchify.args.debug = true;

const sync = browserSync.create();

// Input file.
watchify.args.debug = true;
var bundler = browserify('src/main/js/app.js', watchify.args);

// Babel transform
bundler.transform(babelify.configure({
  sourceMapRelative: 'src/main/js'
}));

// On updates recompile
bundler.on('update', bundle);

function bundle() {
  return bundler.bundle()
    .on('error', function(error){
      console.error( '\nError: ', error.message, '\n');
      this.emit('end');
    })
    .pipe(exorcist('src/main/resources/static/js/bundle.js.map'))
    .pipe(source('bundle.js'))
    .pipe(buffer())
    .pipe(ifElse(process.env.NODE_ENV === 'production', uglify))
    .pipe(gulp.dest('src/main/resources/static/js'));
}

gulp.task('styles', function(){
  return gulp.src('src/main/css/main.scss')
    .pipe(sass())
    .pipe(gulp.dest('src/main/resources/static/css'))
});

gulp.task('default', ['transpile','styles']);

gulp.task('transpile', ['lint'], () => bundle());

gulp.task('lint', () => {
    return gulp.src(['src/main/js/**/*.js', 'gulpfile.babel.js'])
      .pipe(eslint())
      .pipe(eslint.format())
});

gulp.task('serve', ['transpile'], () => sync.init({
  server: 'public',
  port: process.env.PORT || 8000,
  host: process.env.IP || 'localhost'
}));

gulp.task('js-watch', ['transpile'], () => sync.reload());

gulp.task('watch', ['serve'], () => {
  gulp.watch('src/main/js/*', ['js-watch'])
  gulp.watch('src/main/resources/css/main.css', sync.reload)
  gulp.watch('src/main/resources/templates/index.html', sync.reload)
});
