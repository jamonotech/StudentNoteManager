https://github.com/jamonotech/StudentNoteManager

# StudentNoteManager
A desktop application for students marks and deliberation

# Generate ssh keys
ssh-keygen -t rsa -b 4096 -C "your_email_addresse"

cat ~/.ssh/id_rsa.pub

# Clone the project
git clone https://github.com/jamonotech/StudentNoteManager.git

or

git clone git@github.com:jamonotech/StudentNoteManager.git

# To create a branch and switch to it
git checkout -b your_branch_name  // if you just want to switch branch you remove the "-b" option

# To add all your changes to git
git add .

# To commit your changes
git commit -m "your message here"

# To push your changes from your local machine
git push origin develop

# To retrieve latest changes in the main branch
git pull origin develop

# Compile and run project
mvn compile exec:java -Dexec.mainClass="m1.uasz.sn.MainApp"

or

mvn clean install
mvn exec:java -Dexec.mainClass="m1.uasz.sn.MainApp"


Bismillah!
