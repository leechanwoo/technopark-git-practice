const uploader = document.getElementById("uploader");
const imageData = document.getElementById("imageData");
const textArea = document.getElementById("textArea");
const sender = document.getElementById("sender");

const convertBase64 = (file) => {
    return new Promise((resolve, reject) => {
        const fileReader = new FileReader();
        fileReader.readAsDataURL(file);

        fileReader.onload = () => {
            resolve(fileReader.result);
        };

        fileReader.onerror = (error) => {
            reject(error);
        }
    })
}

const uploadImage = async (event) => {
    const file = event.target.files[0];
    const base64 = await convertBase64(file);
    imageData.src = base64;
    textArea.innerText = base64;
};


const sendImage = async () => {
    console.log("send image!");
}

uploader.addEventListener("change", (e) => {
    uploadImage(e);
});

sender.addEventListener('click', () => {
    sendImage();
})