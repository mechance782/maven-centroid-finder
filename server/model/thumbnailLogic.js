export const baseThumbnailGenerator = async(videopath, filename, {
    fs,
    path,
    ffmpeg
}) => {
        const THUMB_DIR = process.env.THUMBNAIL_PATH;
        
        const outputPath = path.join(THUMB_DIR, `${filename}-thumbnail.png`);
        
        return new Promise((resolve, reject) => {
            ffmpeg(videopath)
            .screenshots({
            timestamps: [1],
            filename: path.basename(outputPath),
            folder: process.env.THUMBNAIL_PATH
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