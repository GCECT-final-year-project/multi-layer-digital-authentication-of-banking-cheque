# multi-layer-digital-validation-of-banking-cheque
cheque validation with the help of image steganography (transform domain) and digital sinature (RSA+SHA-256)

Introduction:
        The Cheque System Project is a-based application that allows users to generate and validate cheques with added security features. The system uses transform domain steganography to hide a secret image inside the cheque cover image, and digital signatures to verify the authenticity of the cheque data. The project consists of two main components: the client-side application and the server-side application.

# Client-Side Application:
        The client-side application is responsible for generating the cheque and sending it to the server for validation. The main method of the ClientApp class executes the entire client-side process, which includes the following steps:

        * Reading the cover image file and cheque data text file.

        * Writing the cheque data on the cover image using the TextOnImage class.

        * Hiding the secret image file inside the cover image file using transform domain steganography with the TDS class.

        * Generating a digital signature for the cheque data using the private key and saving it in a file using the DigitalSignature class.

        * Embedding the digital signature in the stego cover image using the ImgOperation class.

        * Sending the required files to the server using the TransferData class.


# Server-Side Application:
        The server-side application is responsible for validating the cheque and comparing the extracted secret image with the original secret image. The main method of the BankServerApp class executes the server-side process, which includes the following steps:

        * The server application starts by reading the stego cover image and the coordinates of the sections on the cheque where text was written.

        * Reads the text data from the cover image using OCR and saves it to a text file.

        * Extracts the digital signature from the stego cover image using LSB steganography.

        * Verifies the digital signature using RSA decryption and the public key.

        * Extracts the secret image from the stego cover image using transform domain steganography.

        * Compares the extracted secret image with the original secret image to check if they match.

        * Generates a report on the validation results and sends it back to the client.




## Files:
        The project includes several files that are used by the client-side and server-side applications. 
        The following is a list of the main files:

# ClientApp.java
        This file is the main class of the client-side application. It contains the main method that executes the entire process of cheque validation and security. The file imports several other classes and methods from other Java files in the project. The main method performs the following tasks:
        * Reads the cover image file and cheque data text file.
        * Writes the cheque data on the cover image using the TextOnImage class.
        * Hides the secret image inside the cover image using the TDS class.
        * Generates a digital signature for the cheque data using the DigitalSignature class.
        * Embeds the digital signature in the cover image using the ImgOperation class.
        * Sends the data to the server using the TransferData class.

# BankServerApp.java
        This file is the main class of the server-side application. It contains the main method that executes the entire process of cheque validation and security. The file imports several other classes and methods from other Java files in the project. The main method performs the following tasks:
        * Reads the stego image file, cheque data text file, and digital signature file.
        * Reads the text data from the cover image using the TextOnImage class.
        * Extracts the digital signature from the cover image using the ImgOperation class.
        * Verifies the digital signature for the cheque data using the DigitalSignature class.
        * Extracts the secret image from the cover image using the TDS class.
        * Compares the extracted secret images with the original secret image using the ImageComparison class.
        * Displaying the validation result on the console.

# DigitalSignature.java
        This file contains methods for generating and verifying digital signatures for cheque data. The file imports several other classes and methods from other Java files in the project. The file contains the following methods:
        * generateHash(): This method generates a hash value for the input data using the SHA-256 algorithm.
        * processFile(): This method processes the input file using the Cipher class and writes the output to the output file.
        * generateSignature(): This method generates a digital signature for the input data using the private key and writes the output to a file.
        * verifySignature(): This method verifies the digital signature for the input data using the public key and returns a boolean value indicating whether the signature is valid or not.

# GenerateKeyPair.java
        This file contains a main method that generates a pair of public and private keys using the RSA algorithm. The keys are saved to separate files in the client-assets/keys directory.

# ImgOperation.java
        This file contains methods for embedding and extracting digital signatures from images. The file imports several other classes and methods from other Java files in the project. The file contains the following methods:

        * embedSignatureInImage(): This method embeds the digital signature in the cover image using the LSB (Least Significant Bit) technique.
        * extractSignatureFromImage(): This method extracts the digital signature from the cover image using the LSB technique.


# TDS.java
        This file contains methods for hiding and extracting secret images inside cover images using transform domain steganography. The file imports several other classes and methods from other Java files in the project. The file contains the following methods:
        * hideSecretImage(): This method hides the secret image inside the cover image using the DWT (Discrete Wavelet Transform) technique.
        * extractSecretImage(): This method extracts the secret image from the cover image using the DWT technique.
# TextOnImage.java
        This file contains methods for writing and reading text data on images using OCR (Optical Character Recognition) technology. The file imports several other classes and methods from other Java files in the project. The file contains the following methods:
        * writeChequeDataOnCover(): This method writes the cheque data on the cover image using the Tesseract OCR engine.
        * readTextDataFromCover(): This method reads the text data from the cover image using the Tesseract OCR engine.

# ImageComparision.java
        This file contains methods for comparing secret images extracted from cover images. The file imports several other classes and methods from other Java files in the project. The file contains the following methods:
        * compareByColorDifference(): This method compares two images based on their color difference and returns a percentage value indicating the similarity.
        * compareImageByDataBufferObjects(): This method compares two images based on their data buffer objects and returns a percentage value indicating the similarity.
        * matchSecretImages(): This method matches the extracted secret images with the original secret image and returns a 3D array containing the matching percentage values for each segment.
        * analyzeMatchResult(): This method analyzes the matching result and returns a boolean value indicating whether the secret images match or not.


# TransferData.java
        This file contains a method for sending data from the client to the server using the FTP (File Transfer Protocol) protocol. The file imports several other classes and methods from other Java files in the project. The file contains the following method:
        * sendToServer(): This method sends the data from the client to the server using the FTP protocol.






Conclusion:
        The Multi-Layer Cheque Authentication System is a secure and efficient way to generate and validate cheques. The project uses advanced techniques such as transform domain steganography and digital signatures to ensure the authenticity and integrity of the cheque data. The project can be further improved by adding more security features and optimizing the code for better performance.




# HOW TO RUN?

step 1 : go to the directory where you want to create the project and launch terminal from there

step 2 : run the command $ git clone https://github.com/GCECT-final-year-project/multi-layer-digital-validation-of-banking-cheque.git 

step 3 : run the command $ cd multi-layer-digital-validation-of-banking-cheque

step 4 : run the command $ code . (assuming vs code and jdk are installed in your machine)

step 5 : in vs code, under JAVA PROJECTS section expand the project and click on the + button beside the Referenced Libraries section. 
        --> a popup window will open, from which go to OCR/lib folder and select all the jars (ctrl + A) and then click on select jar libraries button
        --> all jars will be added to the project

step 6 : for ruuning client side application, execute the Client.java file 

step 7: for running the server side application, execute the BankServerApp.java file

step 8 : All the outputs will be printed in the terminal output window



