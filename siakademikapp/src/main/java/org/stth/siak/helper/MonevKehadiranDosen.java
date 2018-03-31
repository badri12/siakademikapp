// 
// Decompiled by Procyon v0.5.30
// 

package org.stth.siak.helper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.jee.persistence.LogPerkuliahanPersistence;
import java.util.HashMap;
import java.util.Map;
import org.stth.siak.entity.LogPerkuliahan;
import java.util.List;

public class MonevKehadiranDosen
{
    private List<LogPerkuliahan> logs;
    private Map<Integer, RekapKehadiranDosen> rekapMap;
    
    public MonevKehadiranDosen(final LocalDateTime start, final LocalDateTime end) {
        this.rekapMap = new HashMap<>();
        this.logs = LogPerkuliahanPersistence.getLogOnPeriod(start, end);
        this.constructRekap();
    }
    
    private void constructRekap() {
        for (final LogPerkuliahan log : this.logs) {
            final DosenKaryawan dosen = log.getKelasPerkuliahan().getDosenPengampu();
            final int idDosen = dosen.getId();
            RekapKehadiranDosen rkh;
            if (this.rekapMap.containsKey(idDosen)) {
                rkh = this.rekapMap.get(idDosen);
            }
            else {
            	
                rkh = new RekapKehadiranDosen();
                rkh.setDosen(dosen);
                this.rekapMap.put(idDosen, rkh);
            }
            rkh.addLog(log);
        }
    }
    
    public List<RekapKehadiranDosen> getRekap() {
        final ArrayList<RekapKehadiranDosen> l = new ArrayList<RekapKehadiranDosen>(this.rekapMap.values());
        for (RekapKehadiranDosen rk : l) {
			rk.setSKSKP();
		}
        return l;
    }
    
    public class RekapKehadiranDosen
    {
        private DosenKaryawan dosen;
        private int totSKS;
        private List<LogPerkuliahan> logHadir;
        
        public RekapKehadiranDosen() {
            this.logHadir = new ArrayList<>();
            setTotSKS(0);
        }
        
        public DosenKaryawan getDosen() {
            return this.dosen;
        }
        
        public void setDosen(final DosenKaryawan dosen) {
            this.dosen = dosen;
        }
        
        public int getTotSKS() {
			return totSKS;
		}

		public void setTotSKS(int totSKS) {
			this.totSKS = totSKS;
		}

		public List<LogPerkuliahan> getLogHadir() {
            return this.logHadir;
        }
        
        public void setLogHadir(List<LogPerkuliahan> logHadir) {
            this.logHadir = logHadir;
        }
        
        public void addLog(LogPerkuliahan log) {
            this.logHadir.add(log);
            
        }
        public void setSKSKP(){
        	Map<Integer, KelasPerkuliahan> mapKP = new HashMap<>();
            for (LogPerkuliahan lp : logHadir) {
            	KelasPerkuliahan kp = lp.getKelasPerkuliahan();
				mapKP.put(kp.getId(), kp);
			}
            for (KelasPerkuliahan kp : mapKP.values()) {
				setTotSKS(getTotSKS()+kp.getMataKuliah().getSks());
			}
        }
    }
}
