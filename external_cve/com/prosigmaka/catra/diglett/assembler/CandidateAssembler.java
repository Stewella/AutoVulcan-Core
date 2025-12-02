package com.prosigmaka.catra.diglett.assembler;

import com.prosigmaka.catra.diglett.model.dto.CandidateDto;
import com.prosigmaka.catra.diglett.model.dto.TableCandidateDto;
import com.prosigmaka.catra.diglett.model.entity.Candidate;
import com.prosigmaka.catra.diglett.model.enummodel.AvailKandidatEnum;

import org.springframework.stereotype.Component;

@Component
public class CandidateAssembler implements InterfaceAssembler<Candidate, CandidateDto> {

	@Override
	public Candidate fromDto(CandidateDto dto) {
		if (dto == null)
			return null;

		Candidate entity = new Candidate();

		if (dto.getId() != null)
			entity.setId(dto.getId());
		if (dto.getKode() != null)
			entity.setCndkode(dto.getKode());
		if (dto.getNama() != null)
			entity.setNama(dto.getNama());
		if (dto.getJenisKelamin() != null)
			entity.setJenisKelamin(dto.getJenisKelamin());
		if (dto.getTempatLahir() != null)
			entity.setTempatLahir(dto.getTempatLahir());
		if (dto.getTanggalLahir() != null)
			entity.setTanggalLahir(dto.getTanggalLahir());
		if (dto.getAlamat() != null)
			entity.setAlamat(dto.getAlamat());
		if (dto.getEmail() != null)
			entity.setEmailPrimary(dto.getEmail());
		if (dto.getNoHp() != null)
			entity.setNoHpPrimary(dto.getNoHp());
		if(dto.getEkspektasiGaji() !=null)
			entity.setEkspektasiGaji(dto.getEkspektasiGaji());
		if(dto.getWaktuAvailable() !=null)
			entity.setWaktuAvailable(dto.getWaktuAvailable());
		if(dto.getTanggalProses() !=null)
			entity.setTanggalProses(dto.getTanggalProses());
		if(dto.getAvail()!=null)
			entity.setAvailKandidat(dto.getAvail());
		if(dto.getAvail()==null)
			entity.setAvailKandidat(AvailKandidatEnum.AVAILABLE.getValue());

		return entity;
	}

	@Override
	public CandidateDto fromEntity(Candidate entity) {
		if (entity == null)
			return null;
		return CandidateDto.builder()
				.id(entity.getId())
				.kode(entity.getCndkode())
				.nama(entity.getNama())
				.jenisKelamin(entity.getJenisKelamin())
				.tempatLahir(entity.getTempatLahir())
				.tanggalLahir(entity.getTanggalLahir())
				.alamat(entity.getAlamat())
				.tanggalProses(entity.getTanggalProses())
				.waktuAvailable(entity.getWaktuAvailable())
				.email(entity.getEmailPrimary())
				.noHp(entity.getNoHpPrimary())
				.avail(entity.getAvailKandidat())
				.ekspektasiGaji(entity.getEkspektasiGaji())
				.build();
	}

	public TableCandidateDto getTable(Candidate entity) {
		if (entity == null)
			return null;
		String posisi = null;
		String rumpun = null;

		return TableCandidateDto.builder().id(entity.getId()).kode(entity.getCndkode()).nama(entity.getNama())
				.rumpunName(rumpun).posisiName(posisi).email(entity.getEmailPrimary()).noHp(entity.getNoHpPrimary())
				.build();
	}

}
