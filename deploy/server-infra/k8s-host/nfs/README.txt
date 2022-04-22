** NFS 공유 Dir 설정 시

kubernetes master 에 NFS Server 설정하고, Client(nfs-common) 도 설치.

work node 들에도 반드시 NFS Client 가 설치되어 있어야 kubernetes 에서 nfs 연결 가능!!.



nfs 서버 설정 nfs client 깔아서 연결  확인.

sudo mkdir /mnt/nfs-test
sudo mount 192.168.0.241:/nfs-share/jenkins /mnt/nfs-test
ls /mnt/nfs-test
sudo umount /mnt/nfs-test
