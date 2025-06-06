export const baseThumbnailGenerator = async(videopath, filename, {
    fs,
    path,
    ffmpeg
}) => {
        const THUMB_DIR = path.resolve(process.env.THUMBNAIL_PATH || './public/thumbnails')
        
        const outputPath = path.join(THUMB_DIR, `${filename}-thumbnail.png`);
        
        if (!fs.existsSync(THUMB_DIR)){
            fs.mkdirSync(THUMB_DIR, {recursive: true});
        }

        return new Promise((resolve, reject) => {
            ffmpeg(videopath)
            .screenshots({
            timestamps: [1],
            filename: path.basename(outputPath),
            folder: THUMB_DIR
            })
            .on('end', () => resolve(outputPath))
            .on('error', e => {
                console.error('FFmpeg error: ', e);
                e.message=("Error generating thumbnail: ", e);
                e.status =(500);
                reject(e);
            });
        }); 
}

export default {baseThumbnailGenerator};