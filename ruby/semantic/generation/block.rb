class BlockCode
  def before
    []
  end

  def after
    []
  end

  def code
    last=@statements[-1]
      
    cs=@statements[0..-2].map{|s|
      s.before+s.code+s.after
    }
    if last
      cs+=last.before+last.code
    end  
    cs=cs.flatten
    cs
  end

  def value
    @statements[-1].value
  end
end