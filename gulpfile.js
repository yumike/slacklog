var gulp = require('gulp');
var stylus = require('gulp-stylus');
var nib = require('nib');

gulp.task('stylus', function() {
  gulp.src('assets/application.styl')
    .pipe(stylus({use: [nib()], errors: true}))
    .pipe(gulp.dest('resources/public'));
});

gulp.task('watch', function() {
  gulp.watch('assets/**/*.styl', ['stylus']);
});

gulp.task('default', ['stylus']);
