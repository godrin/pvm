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
    checkExceptions(cs)
  end

  def value
    @statements[-1].value
  end

  private

  def checkExceptions(array)
    a=[]
    noadd=false
    array.each{|e|
      if noadd
        if e=~/ *end/
          noadd=false
          a<<e
        end
      else
        a<<e
        noadd=true if e=~/[elf]return .*/
      end
    }
    a
  end
end